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

    public static final long MINUTE = 60000l;
    public static final int RATE_LIMIT = 3;
    private Map<String, List<Long>> limiter = new HashMap<>();

    public static final String AUTHENTICATION_HEADER = "Authorization";

    @Override
    public void filter(ContainerRequestContext containerRequest) {

        String authCredentials = containerRequest
                .getHeaderString(AUTHENTICATION_HEADER);

        if (this.exceedLimit(authCredentials)) {

            containerRequest.abortWith(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Too many request")
                    .build());
        }

    }

    private Boolean exceedLimit(String authtorization) {
        Long timeMillis = System.currentTimeMillis();

        if(limiter.containsKey(authtorization)){
            List<Long> arr = limiter.get(authtorization);
            if (arr.size() >= RATE_LIMIT) {
                if(timeMillis - arr.get(0) > MINUTE) {
                    arr.remove(0);
                    arr.add(timeMillis);
                } else {
                    return true;
                }
            } else {
                arr.add(timeMillis);
            }
        } else {
            List<Long> list = new ArrayList<>();
            list.add(timeMillis);
            limiter.put(authtorization, list);
        }

        return false;
    }
}