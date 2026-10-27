package com.futy.tenancy;

import java.util.Map;
import java.util.UUID;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl
		implements CurrentTenantIdentifierResolver<UUID>, HibernatePropertiesCustomizer {

	/**
	 * Sentinel used only while the EntityManagerFactory bootstraps (no request tenant yet).
	 */
	static final UUID BOOTSTRAP_TENANT = UUID.fromString("00000000-0000-0000-0000-000000000000");

	@Override
	public UUID resolveCurrentTenantIdentifier() {
		UUID tenantId = TenantContext.get();
		return tenantId != null ? tenantId : BOOTSTRAP_TENANT;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
	}
}
