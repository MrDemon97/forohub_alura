package com.alura.forohub.forohub.services;

import com.alura.forohub.forohub.models.Topico;
import com.alura.forohub.forohub.repositories.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    public List<Topico> listarTopicos(){
        return topicoRepository.findAll();
    }

    @Transactional
    public Topico crearTopico(Topico topico){
        if (topicoRepository.existsByTituloAndMensaje(topico.getTitulo(),topico.getMensaje())) {
            throw new IllegalArgumentException("El tópico ya existe");
        }
        return topicoRepository.save(topico);
    }

    @Transactional
    public void eliminarTopico(Long id){
        topicoRepository.deleteById(id);
    }

}
