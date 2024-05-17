package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.entity.ParkingEntity;
import fr.rp.springapiparc.repository.ParkingRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private ParkingRepository repository;

    @GetMapping("/{id}")
    public ParkingEntity findById(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoResultException("Cet identifiant n'existe pas !"));
    }

    @GetMapping("/")
    public Collection<ParkingEntity> findBooks() {
        return repository.findAll();
    }

}

