package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.controller.exception.ApiError;
import fr.rp.springapiparc.dto.in.RegionInDto;
import fr.rp.springapiparc.dto.out.RegionOutDto;
import fr.rp.springapiparc.entity.RegionEntity;
import fr.rp.springapiparc.repository.RegionRepository;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegionControllerTest {
    @Autowired
    RegionRepository RegionRepository;
    List<RegionEntity> listRegionEntity = new ArrayList<>();
    List<RegionOutDto> listRegionOutDto = new ArrayList<>();
    Random rand = new Random();
    RegionOutDto testControleRegionOutDto;
    static int indexCreateMethod;

    @BeforeEach
    void setUp() {
        listRegionEntity = RegionRepository.findAll();
        for (RegionEntity RegionEntity : listRegionEntity) {
            RegionOutDto RegionOutDto = new RegionOutDto(RegionEntity);
            listRegionOutDto.add(RegionOutDto);
        }
        Optional<RegionEntity> optionalRegionEntity = RegionRepository.findById(1);
        if (optionalRegionEntity.isPresent()) {
            RegionEntity testControleRegionEntity = optionalRegionEntity.get();
            testControleRegionOutDto = new RegionOutDto(testControleRegionEntity);
        }
    }
    @Test
    void getListRegion() {
        List<RegionOutDto> apiResponse = given()
                .when()
                .get("http://localhost:8084/api/regions")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", RegionOutDto.class);

        assertEquals(listRegionOutDto.size(), apiResponse.size());

        int indexControl = rand.nextInt(listRegionOutDto.size());

        assertEquals(listRegionOutDto.get(indexControl).getId(), apiResponse.get(indexControl).getId());
        assertEquals(listRegionOutDto.get(indexControl).getNomRegion(), apiResponse.get(indexControl).getNomRegion());
        assertEquals(listRegionOutDto.get(indexControl).getSlugRegion(), apiResponse.get(indexControl).getSlugRegion());


    }


    @Test
    void getRegionById200() {
        RegionOutDto apiResponse = given()
                .when()
                .get("http://localhost:8084/api/regions/" + 1)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", RegionOutDto.class);

        assertEquals(testControleRegionOutDto.getId(), apiResponse.getId());
        assertEquals(testControleRegionOutDto.getNomRegion(), apiResponse.getNomRegion());
        assertEquals(testControleRegionOutDto.getSlugRegion(), apiResponse.getSlugRegion());

    }
    @Test
    void getRegionById404() {
        String errorMessage = given()
                .when()
                .get("http://localhost:8084/api/regions/" + 1000000000)
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertEquals("Region non trouvée", errorMessage);
    }
    @Test
    @Transactional
    @Order(1)

    void createRegion201() {
        RegionInDto RegionInDto = new RegionInDto("Test Create Region");

        RegionOutDto apiResponse = given()
                .contentType(ContentType.JSON)
                .body(RegionInDto)
                .when()
                .post("http://localhost:8084/api/regions")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", RegionOutDto.class);

        assertEquals(RegionInDto.getNomRegion(), apiResponse.getNomRegion());
        indexCreateMethod = apiResponse.getId();

    }

    @Test
    @Transactional
    void createRegion400() {
        RegionInDto RegionInDto = new RegionInDto("Test Create Region1234@");

        ApiError errorResponse = given()
                .contentType(ContentType.JSON)
                .body(RegionInDto)
                .when()
                .post("http://localhost:8084/api/regions")
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
        assertEquals("champ : nomRegion - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
    }


    @Test
    @Transactional
    @Order(2)
    void updateRegion200() {
        RegionInDto RegionInDto = new RegionInDto("Test Update Region");

        RegionOutDto apiResponse = given()
                .contentType(ContentType.JSON)
                .body(RegionInDto)
                .when()
                .put("http://localhost:8084/api/regions/"+ indexCreateMethod)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", RegionOutDto.class);

        assertEquals(RegionInDto.getNomRegion(), apiResponse.getNomRegion());
    }

    @Test
    @Transactional
    @Order(3)
    void updateRegion400() {
        RegionInDto RegionInDto = new RegionInDto("Test Update Region@123");

        ApiError errorResponse = given()
                .contentType(ContentType.JSON)
                .body(RegionInDto)
                .when()
                .put("http://localhost:8084/api/regions/" + indexCreateMethod)
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
        assertEquals("champ : nomRegion - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
    }
    @Test
    @Transactional
    void updateRegion404() {
        RegionInDto RegionInDto = new RegionInDto("Test Update Region");

        String errorMessage = given()
                .contentType(ContentType.JSON)
                .body(RegionInDto)
                .when()
                .put("http://localhost:8084/api/regions/" + 1000000000)
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertEquals("Région non trouvée", errorMessage);
    }

    @Test
    @Transactional
    void deleteRegion404() {
        String errorMessage = given()
                .when()
                .delete("http://localhost:8084/api/regions/" + 1000000000)
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertEquals("Région non trouvée", errorMessage);
    }

    @Test
    @Transactional
    @Order(4)
    void deleteRegion200() {
        String errorMessage = given()
                .when()
                .delete("http://localhost:8084/api/regions/" + indexCreateMethod)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertEquals("Région Supprimée", errorMessage);
    }

}