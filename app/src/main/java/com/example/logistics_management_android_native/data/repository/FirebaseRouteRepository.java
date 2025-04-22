package com.example.logistics_management_android_native.data.repository;

import com.example.logistics_management_android_native.data.model.Package;
import com.example.logistics_management_android_native.data.model.Route;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirebaseRouteRepository {
    private final FirebaseFirestore db;
    private final CollectionReference routesCollection;
    private final CollectionReference packagesCollection;

    public FirebaseRouteRepository() {
        db = FirebaseFirestore.getInstance();
        routesCollection = db.collection("Ruta");
        packagesCollection = db.collection("Paquete");
    }

    private Route documentToRoute(DocumentSnapshot document) {
        Map<String, Object> destinoMap = (Map<String, Object>) document.get("destino");
        Map<String, Object> fechasMap = (Map<String, Object>) document.get("fechas");

        Route.Destino destino = new Route.Destino();
        destino.setLat((double) destinoMap.get("lat"));
        destino.setLon((double) destinoMap.get("lon"));

        Route.Fechas fechas = new Route.Fechas();
        fechas.setInicioRepartir(String.valueOf(fechasMap.get("inicioRepartir")));
        fechas.setFinRepartir(String.valueOf(fechasMap.get("finRepartir")));

        return Route.builder()
                .uuid(document.getId())
                .cliente((String) document.get("cliente"))
                .repartidorUserID((String) document.get("repartidorUserID"))
                .estado((String) document.get("estado"))
                .destino(destino)
                .fechas(fechas)
                .build();
    }

    private Package documentToPackage(DocumentSnapshot document) {
        Map<String, Object> ubicacionMap = (Map<String, Object>) document.get("ubicacion");
        Map<String, Object> tamañoMap = (Map<String, Object>) document.get("tamaño");

        Package.Ubicacion ubicacion = new Package.Ubicacion();
        ubicacion.setDeposito(((Number) ubicacionMap.get("deposito")).intValue());
        ubicacion.setSector(((Number) ubicacionMap.get("sector")).intValue());
        ubicacion.setEstante(((Number) ubicacionMap.get("estante")).intValue());

        Package.Tamaño tamaño = new Package.Tamaño();
        tamaño.setAncho(((Number) tamañoMap.get("ancho")).intValue());
        tamaño.setLargo(((Number) tamañoMap.get("largo")).intValue());
        tamaño.setAlto(((Number) tamañoMap.get("alto")).intValue());

        return Package.builder()
                .uuid(document.getId())
                .nombre((String) document.get("nombre"))
                .descripcion((String) document.get("descripcion"))
                .ubicacion(ubicacion)
                .tamaño(tamaño)
                .peso(((Number) document.get("peso")).doubleValue())
                .rutaRef((String) document.get("rutaRef"))
                .build();
    }

    public Task<List<Route>> getRoutesByRepartidor(String repartidorUserID, int limit) {
        return routesCollection
                .whereEqualTo("repartidorUserID", repartidorUserID)
                .limit(limit)
                .get()
                .continueWith(task -> {
                    List<Route> routes = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            routes.add(documentToRoute(document));
                        }

                        routes.sort((r1, r2) -> {
                            Map<String, Integer> priority = new HashMap<>();
                            priority.put("en progreso", 1);
                            priority.put("pendiente", 2);
                            priority.put("completado", 3);

                            int r1Priority = priority.getOrDefault(r1.getEstado().toLowerCase(), Integer.MAX_VALUE);
                            int r2Priority = priority.getOrDefault(r2.getEstado().toLowerCase(), Integer.MAX_VALUE);

                            return Integer.compare(r1Priority, r2Priority);
                        });
                    }
                    return routes;
                });
    }
    public Task<List<Route>> getRoutesByStatusAndRepartidor(String estado, String repartidorUserID) {
        Query query;
        if ("Todas".equals(estado)) {
            query = routesCollection.whereIn("estado", Arrays.asList("disponible", "en progreso", "pendiente"));
        } else {
            query = routesCollection.whereEqualTo("estado", estado)
                    .whereEqualTo("repartidorUserID", "disponible".equals(estado) ? "" : repartidorUserID);
        }

        return query.get().continueWith(task -> {
            List<Route> routes = new ArrayList<>();
            if (!task.isSuccessful()) return routes;

            for (DocumentSnapshot doc : task.getResult()) {
                String docEstado = doc.getString("estado");
                String docRepartidor = doc.getString("repartidorUserID");

                if ("Todas".equals(estado)) {
                    boolean isDisponibleSinAsignar = "disponible".equals(docEstado) && "".equals(docRepartidor);
                    boolean esDeEsteRepartidor = !"disponible".equals(docEstado) && repartidorUserID.equals(docRepartidor);

                    if (isDisponibleSinAsignar || esDeEsteRepartidor) {
                        routes.add(documentToRoute(doc));
                    }
                } else {
                    routes.add(documentToRoute(doc));
                }
            }

            return routes;
        });
    }

    public Task<List<Route>> getAvailableRoutes() {
        return routesCollection
                .whereEqualTo("estado", "disponible")
                .whereEqualTo("repartidorUserID", "")
                .get()
                .continueWith(task -> {
                    List<Route> routes = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            routes.add(documentToRoute(document));
                        }
                    }
                    return routes;
                });
    }

    public Task<List<Package>> getPackagesForRoute(String routeId) {
        return packagesCollection
                .whereEqualTo("rutaRef", routeId)
                .get()
                .continueWith(task -> {
                    List<Package> packages = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            packages.add(documentToPackage(document));
                        }
                    }
                    return packages;
                });
    }
}