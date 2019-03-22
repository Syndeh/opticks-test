package com.opticks.config;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Provider
public class RateLimitFilter implements ContainerRequestFilter {

    //tiempo limite para cada usuario, en este caso 1 minuto expresado en milisegundos
    public static final long MINUTE = 60000l;
    //cantidad de intentos que puede hacer en el tiempo limite
    public static final int RATE_LIMIT = 3;
    //estructura de datos para determinar si un usuario excedio el limite de peticiones
    private Map<String, List<Long>> limiter = new HashMap<>();
    //header para identificar al usuario
    public static final String AUTHENTICATION_HEADER = "Authorization";

    @Override
    public void filter(ContainerRequestContext containerRequest) {

        //se obtiene el usuario autenticado
        String authCredentials = containerRequest
                .getHeaderString(AUTHENTICATION_HEADER);

        //se verifica si el usuario excede el limite de  request, si es true se aborta la peticion
        if (this.exceedLimit(authCredentials)) {
            //se retorna una respuesta con status 429
            containerRequest.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity("Too many request")
                    .build());
        }

    }

    private Boolean exceedLimit(String authtorization) {
        //se obtiene el momento en que se consulta la peticion
        Long timeMillis = System.currentTimeMillis();

        //se verifica si el usuario ya esta identificado
        if(limiter.containsKey(authtorization)){
            //en caso de encontrar el usuario se obtiene las peticiones realizadas
            List<Long> arr = limiter.get(authtorization);
            //se verifica si excede el numero de peticiones limite
            if (arr.size() >= RATE_LIMIT) {
                //en caso de exceder el numero de peticiones se verifica si esta dentro del tiempo limite
                if(timeMillis - arr.get(0) > MINUTE) {
                    //en caso de estar fuera del tiempo limite se procede a sacar el primer registro de tiempo
                    arr.remove(0);
                    //y se agrega el nuevo registo de tiempo
                    arr.add(timeMillis);
                } else {
                    //en caso de estar dentro del tiempo limite y excedido de cantidad de peticiones
                    //se retorna qeu si esta excedido el limite
                    return true;
                }
            } else {
                //en caso de no exceder el numero se agrega la peticion en el momento determinado
                arr.add(timeMillis);
            }
        } else {
            //en caso que no este identificado se agrega a la estructura de datos
            List<Long> list = new ArrayList<>();
            list.add(timeMillis);
            //para simplificar las cosas utilice un map, pero podria ser una base de datos el cual verifique las peticiones
            limiter.put(authtorization, list);
        }
        //por defecto se entiende que no esta excedido del limite de peticiones
        return false;
    }
}