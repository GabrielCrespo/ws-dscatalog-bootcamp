package com.gabriel.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.dscatalog.dto.ProductDTO;
import com.gabriel.dscatalog.factory.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;

        productDTO = Factory.createProductDTO();
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders.get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(countTotalProducts));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Macbook Pro"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("PC Gamer"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingId));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedName));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedDescription));

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
