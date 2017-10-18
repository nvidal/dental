package uy.dental.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(uy.dental.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(uy.dental.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".pacientes", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Procedimiento.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Pieza.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Saldo.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".procedimientos", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".saldos", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".cuentas", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Cuenta.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Presupuesto.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".presupuestos", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".tratamientos", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".diagnosticos", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Diagnostico.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Tratamiento.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".pagos", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Pago.class.getName(), jcacheConfiguration);
            cm.createCache(uy.dental.domain.Paciente.class.getName() + ".notas", jcacheConfiguration);
            cm.createCache(uy.dental.domain.Nota.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
