package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.Item;
import com.exemplo.softwarelab.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Operation(summary = "Adicionar um item à lista", description = "Adiciona um novo item a uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<Item> addItemToList(@RequestParam long listId, @RequestParam long productId, @RequestParam int quantity) {
        Item createdItem = itemService.addItemToList(listId, productId, quantity);
        return ResponseEntity.ok(createdItem);
    }

    @Operation(summary = "Remover um item da lista", description = "Remove um item de uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @DeleteMapping
    public ResponseEntity<Void> removeItemFromList(@RequestParam long listId, @RequestParam long productId) {
        try {
            itemService.removeItemFromList(listId, productId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar a quantidade de um item", description = "Atualiza a quantidade de um item em uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @PutMapping
    public ResponseEntity<Item> updateItemQuantity(@RequestParam long listId, @RequestParam long productId, @RequestParam int quantity) {
        try {
            Item updatedItem = itemService.updateItemQuantity(listId, productId, quantity);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar todos os itens de uma lista", description = "Retorna todos os itens de uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens encontrados"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @GetMapping
    public ResponseEntity<List<Item>> getAllListItems(@RequestParam long listId) {
        List<Item> items = itemService.getAllListItems(listId);
        return ResponseEntity.ok(items);
    }
}