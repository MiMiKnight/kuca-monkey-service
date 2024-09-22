package cn.mimiknight.developer.monkey.core.rest.handler.health;

import cn.mimiknight.developer.kuca.spring.ecology.handler.KucaEcologyRequestHandler;
import cn.mimiknight.developer.monkey.core.rest.model.request.HealthCheckRequest;
import cn.mimiknight.developer.monkey.core.rest.model.response.HealthCheckResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckHandler implements KucaEcologyRequestHandler<HealthCheckRequest, HealthCheckResponse> {

    @Override
    public HealthCheckResponse handle(HealthCheckRequest request) {
        HealthCheckResponse response = new HealthCheckResponse();
        response.setServiceName("kuca-monkey-service");
        response.setRedisStatus(true);
        response.setDatabaseStatus(true);
        return response;
    }
}
