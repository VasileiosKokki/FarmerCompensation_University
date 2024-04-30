package dit.hua.distributedSystems.project.demo.config;

import dit.hua.distributedSystems.project.demo.entity.Application;
import dit.hua.distributedSystems.project.demo.entity.MUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Application.class);
        config.exposeIdsFor(MUser.class);
    }
}