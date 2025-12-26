package com.example.demo.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;

// This custom base repository fixes the "FullIntegrationTest$1 is not an entity" error
// by detecting the anonymous subclass created by the test and converting it
// to a proper Entity instance before saving.
public class CleanSaveRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    public CleanSaveRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public <S extends T> S save(S entity) {
        // Check if the object is an anonymous subclass (e.g., created by {{ }})
        if (entity.getClass().isAnonymousClass()) {
            try {
                // Get the real entity class (the superclass)
                Class<?> superclass = entity.getClass().getSuperclass();
                
                // Create a clean instance of the real entity
                S cleanEntity = (S) superclass.getDeclaredConstructor().newInstance();
                
                // Copy all data from the "bad" test object to the "clean" entity
                BeanUtils.copyProperties(entity, cleanEntity);
                
                // Save the clean entity instead
                return super.save(cleanEntity);
            } catch (Exception e) {
                // If fix fails, proceed with original (will likely fail, but we tried)
                e.printStackTrace();
            }
        }
        // If it's a normal object, save it normally
        return super.save(entity);
    }
}