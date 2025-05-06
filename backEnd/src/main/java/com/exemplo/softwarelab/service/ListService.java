package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.List;
import com.exemplo.softwarelab.repository.ListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListService {

    @Autowired
    private ListRepository listRepository;

    public List createList(List list) {
        return listRepository.save(list);
    }

    public Optional<List> getListById(Long id) {
        return listRepository.findById(id);
    }

    public List updateList(Long id, List updatedList) {
        Optional<List> existingList = listRepository.findById(id);
        if (existingList.isPresent()) {
            List list = existingList.get();
            list.setName(updatedList.getName());
            return listRepository.save(list);
        }
        throw new RuntimeException("Lista não encontrada com ID: " + id);
    }

    public void deleteList(Long id) {
        if (listRepository.existsById(id)) {
            listRepository.deleteById(id);
        } else {
            throw new RuntimeException("Lista não encontrada com ID: " + id);
        }
    }

    public java.util.List<List> getAllLists() {
        return listRepository.findAll();
    }
}
