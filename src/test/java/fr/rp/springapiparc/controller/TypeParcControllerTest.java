package fr.rp.springapiparc.controller;


import fr.rp.springapiparc.controller.exception.ApiError;
import fr.rp.springapiparc.dto.in.TypeParcInDto;
import fr.rp.springapiparc.dto.out.TypeParcOutDto;
import fr.rp.springapiparc.entity.TypeParcEntity;
import fr.rp.springapiparc.repository.TypeParcRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TypeParcControllerTest {

    @Autowired
    TypeParcRepository typeParcRepository;
    List<TypeParcEntity> listTypeParcEntity = new ArrayList<>();
    List<TypeParcOutDto> listTypeParcOutDto = new ArrayList<>();
    Random rand = new Random();
    TypeParcOutDto testControleTypeParcOutDto;
    static int indexCreateMethod;

    @BeforeEach
    void setUp() {
        listTypeParcEntity = typeParcRepository.findAll();
        for (TypeParcEntity typeParcEntity : listTypeParcEntity) {
            TypeParcOutDto typeParcOutDto = new TypeParcOutDto(typeParcEntity);
            listTypeParcOutDto.add(typeParcOutDto);
        }
        Optional<TypeParcEntity> optionalTypeParcEntity = typeParcRepository.findById(1);
        if (optionalTypeParcEntity.isPresent()) {
            TypeParcEntity testControletypeParcEntity = optionalTypeParcEntity.get();
            testControleTypeParcOutDto = new TypeParcOutDto(testControletypeParcEntity);
        }
    }
    @Test
    void getListTypeParc() {
        List<TypeParcOutDto> apiResponse = given()
                .when()
                .get("http://localhost:8081/api/types")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", TypeParcOutDto.class);

        assertEquals(listTypeParcOutDto.size(), apiResponse.size());

        int indexControl = rand.nextInt(listTypeParcOutDto.size());

        assertEquals(listTypeParcOutDto.get(indexControl).getId(), apiResponse.get(indexControl).getId());
        assertEquals(listTypeParcOutDto.get(indexControl).getLibelleTypeParc(), apiResponse.get(indexControl).getLibelleTypeParc());

    }


    @Test
    void getTypeParcById200() {
        TypeParcOutDto apiResponse = given()
                .when()
                .get("http://localhost:8081/api/types/" + 1)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", TypeParcOutDto.class);

        assertEquals(testControleTypeParcOutDto.getId(), apiResponse.getId());
        assertEquals(testControleTypeParcOutDto.getLibelleTypeParc(), apiResponse.getLibelleTypeParc());
        assertEquals(testControleTypeParcOutDto.getSlugType(), apiResponse.getSlugType());
    }
    @Test
    void getTypeParcById404() {
        String errorMessage = given()
                .when()
                .get("http://localhost:8081/api/types/" + 1000000000)
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertEquals("Type non trouvé", errorMessage);
    }
    @Test
    @Transactional
    @Order(1)

    void createType201() {
        TypeParcInDto typeParcInDto = new TypeParcInDto("Test Create TypeParc");

        TypeParcOutDto apiResponse = given()
                .contentType(ContentType.JSON)
                .body(typeParcInDto)
                .when()
                .post("http://localhost:8081/api/types")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", TypeParcOutDto.class);

        assertEquals(typeParcInDto.getLibelleTypeParc(), apiResponse.getLibelleTypeParc());
        indexCreateMethod = apiResponse.getId();

    }

    @Test
    @Transactional
    void createType400() {
        TypeParcInDto typeParcInDto = new TypeParcInDto("Test Create TypeParc1234@");

        ApiError errorResponse = given()
                .contentType(ContentType.JSON)
                .body(typeParcInDto)
                .when()
                .post("http://localhost:8081/api/types")
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
        assertEquals("champ : libelleTypeParc - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
    }


    @Test
    @Transactional
    @Order(2)
    void updateTypeParc200() {
        TypeParcInDto typeParcInDto = new TypeParcInDto("Test Update TypeParc");

        TypeParcOutDto apiResponse = given()
                .contentType(ContentType.JSON)
                .body(typeParcInDto)
                .when()
                .put("http://localhost:8081/api/types/"+ indexCreateMethod)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", TypeParcOutDto.class);

        assertEquals(typeParcInDto.getLibelleTypeParc(), apiResponse.getLibelleTypeParc());
    }

    @Test
    @Transactional
    @Order(3)
    void updateTypeParc400() {
        TypeParcInDto typeParcInDto = new TypeParcInDto("Test Update TypeParc@123");

        ApiError errorResponse = given()
                .contentType(ContentType.JSON)
                .body(typeParcInDto)
                .when()
                .put("http://localhost:8081/api/types/" + indexCreateMethod)
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
        assertEquals("champ : libelleTypeParc - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
    }
    @Test
    @Transactional
    void updateTypeParc404() {
        TypeParcInDto typeParcInDto = new TypeParcInDto("Test Update TypeParc");

        String errorMessage = given()
                .contentType(ContentType.JSON)
                .body(typeParcInDto)
                .when()
                .put("http://localhost:8081/api/types/" + 1000000000)
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertEquals("Type non trouvé", errorMessage);
    }

    @Test
    @Transactional
    void deleteType404() {
        String errorMessage = given()
                .when()
                .delete("http://localhost:8081/api/types/" + 1000000000)
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertEquals("Type non trouvé", errorMessage);
    }

    @Test
    @Transactional
    @Order(4)
    void deleteType200() {
        String errorMessage = given()
                .when()
                .delete("http://localhost:8081/api/types/" + indexCreateMethod)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertEquals("Suppression du type", errorMessage);
    }

}
