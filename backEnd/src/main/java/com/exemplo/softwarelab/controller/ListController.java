package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.List;
import com.exemplo.softwarelab.service.ListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    @Autowired
    private ListService listService;

    @Operation(summary = "Criar uma nova lista", description = "Cria uma nova lista com o nome fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<List> createList(@RequestBody List list) {
        List createdList = listService.createList(list);
        return ResponseEntity.ok(createdList);
    }

    @Operation(summary = "Abrir uma lista", description = "Busca uma lista pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List> getListById(@PathVariable Long id) {
        Optional<List> list = listService.getListById(id);
        return list.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Editar uma lista", description = "Atualiza o nome de uma lista existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<List> updateList(@PathVariable Long id, @RequestBody List updatedList) {
        try {
            List list = listService.updateList(id, updatedList);
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remover uma lista", description = "Deleta uma lista pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lista removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        try {
            listService.deleteList(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar todas as listas", description = "Retorna todas as listas disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listas encontradas")
    })
    @GetMapping
    public ResponseEntity<java.util.List<List>> getAllLists() {
        java.util.List<List> lists = listService.getAllLists();
        return ResponseEntity.ok(lists);
    }
}
