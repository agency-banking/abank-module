package com.agencybanking.core.module;

import com.agencybanking.core.services.Crud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

/**
 * @author dubic
 */
@Service
@Slf4j
@CacheConfig(cacheNames={"module"})
public class ModuleService implements Crud<Module, String> {

    private ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Module create(Module module) {
        return this.moduleRepository.save(module);
    }

    @Override
    public Module update(Module module) {
        Module found = this.moduleRepository.findById(module.getCode()).orElseThrow(() -> new IllegalStateException("Resource not found"));
        found.copyForUpdate(module);
        return this.moduleRepository.save(found);
    }

    @Override
    public void delete(Module module) {
        moduleRepository.delete(module);
    }

    @Override
    public Optional<Module> findById(String id) {
        return moduleRepository.findById(id);
    }

    @Override
    public Page<Module> query(Module module, PageRequest p) {
        if (ObjectUtils.isEmpty(module)) {
            return moduleRepository.findAll(p);
        }
        return moduleRepository.findAll(Example.of(module), p);
    }

    @Override
    @Cacheable
    public Collection<Module> list() {
        return moduleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public boolean exists(Module module, String id) {
        return moduleRepository.exists(Example.of(module));
    }

}
