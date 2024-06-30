package com.example.demo.services;

import com.example.demo.controller.BaseControllerImpl;
import com.example.demo.entities.MetodoError;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.MetodoErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetodoErrorServiceImpl extends BaseServiceImpl<MetodoError,Long> implements MetodoErrorService {
    @Autowired
    private MetodoErrorRepository metodoErrorRepository;

    public MetodoErrorServiceImpl(BaseRepository<MetodoError, Long> baseRepository, MetodoErrorRepository metodoErrorRepository) {
        super(baseRepository);
        this.metodoErrorRepository = metodoErrorRepository;
    }
}
