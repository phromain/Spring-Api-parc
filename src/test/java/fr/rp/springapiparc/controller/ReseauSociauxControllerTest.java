package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.controller.exception.ApiError;
import fr.rp.springapiparc.dto.in.ReseauSociauxInDto;
import fr.rp.springapiparc.dto.out.ReseauSociauxOutDto;
import fr.rp.springapiparc.entity.ReseauSociauxEntity;
import fr.rp.springapiparc.repository.ReseauSociauxRepository;
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
class ReseauSociauxControllerTest {


        @Autowired
        ReseauSociauxRepository reseauSociauxRepository;
        List<ReseauSociauxEntity> listReseauSociauxEntity = new ArrayList<>();
        List<ReseauSociauxOutDto> listReseauSociauxOutDto = new ArrayList<>();
        Random rand = new Random();
        ReseauSociauxOutDto testControleReseauSociauxOutDto;
        static int indexCreateMethod;
        private static String apiKey = "JKLzcla88cadPbFS1d7jPTMODXQdkW05";

        @BeforeEach
        void setUp() {
            listReseauSociauxEntity = reseauSociauxRepository.findAll();
            for (ReseauSociauxEntity parkingEntity : listReseauSociauxEntity) {
                ReseauSociauxOutDto parkingOutDto = new ReseauSociauxOutDto(parkingEntity);
                listReseauSociauxOutDto.add(parkingOutDto);
            }
            Optional<ReseauSociauxEntity> optionalReseauSociauxEntity = reseauSociauxRepository.findById(1);
            if (optionalReseauSociauxEntity.isPresent()) {
                ReseauSociauxEntity testControleparkingEntity = optionalReseauSociauxEntity.get();
                testControleReseauSociauxOutDto = new ReseauSociauxOutDto(testControleparkingEntity);
            }
        }
        @Test
        void getListReseauSociaux() {
            List<ReseauSociauxOutDto> apiResponse = given()
                    .header("apikey", apiKey)
                    .when()
                    .get("http://localhost:8081/api/reseausociaux")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", ReseauSociauxOutDto.class);

            assertEquals(listReseauSociauxOutDto.size(), apiResponse.size());

            int indexControl = rand.nextInt(listReseauSociauxOutDto.size());

            assertEquals(listReseauSociauxOutDto.get(indexControl).getId(), apiResponse.get(indexControl).getId());
            assertEquals(listReseauSociauxOutDto.get(indexControl).getLibelleReseau(), apiResponse.get(indexControl).getLibelleReseau());

        }


        @Test
        void getReseauSociauxById200() {
            ReseauSociauxOutDto apiResponse = given()
                    .header("apikey", apiKey)
                    .when()
                    .get("http://localhost:8081/api/reseausociaux/" + 1)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ReseauSociauxOutDto.class);

            assertEquals(testControleReseauSociauxOutDto.getId(), apiResponse.getId());
            assertEquals(testControleReseauSociauxOutDto.getLibelleReseau(), apiResponse.getLibelleReseau());

        }
        @Test
        void getReseauSociauxById404() {
            String errorMessage = given()
                    .header("apikey", apiKey)
                    .when()
                    .get("http://localhost:8081/api/reseausociaux/" + 1000000000)
                    .then()
                    .statusCode(404)
                    .extract()
                    .body()
                    .asString();

            assertEquals("ReseauSociaux non trouvé", errorMessage);
        }
        @Test
        @Transactional
        @Order(1)

        void createReseauSociaux201() {
            ReseauSociauxInDto parkingInDto = new ReseauSociauxInDto("Test Create ReseauSociaux");

            ReseauSociauxOutDto apiResponse = given()
                    .header("apikey", apiKey)
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .post("http://localhost:8081/api/reseausociaux")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ReseauSociauxOutDto.class);

            assertEquals(parkingInDto.getLibelleReseau(), apiResponse.getLibelleReseau());
            indexCreateMethod = apiResponse.getId();

        }

        @Test
        @Transactional
        void createReseauSociaux400() {
            ReseauSociauxInDto parkingInDto = new ReseauSociauxInDto("Test Create ReseauSociaux1234@");

            ApiError errorResponse = given()
                    .header("apikey", apiKey)
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .post("http://localhost:8081/api/reseausociaux")
                    .then()
                    .statusCode(400)
                    .extract()
                    .body()
                    .as(ApiError.class);

            assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
            assertEquals("champ : libelleReseau - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
        }


        @Test
        @Transactional
        @Order(2)
        void updateReseauSociaux200() {
            ReseauSociauxInDto parkingInDto = new ReseauSociauxInDto("Test Update ReseauSociaux");

            ReseauSociauxOutDto apiResponse = given()
                    .header("apikey", apiKey)
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .put("http://localhost:8081/api/reseausociaux/"+ indexCreateMethod)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ReseauSociauxOutDto.class);

            assertEquals(parkingInDto.getLibelleReseau(), apiResponse.getLibelleReseau());
        }

        @Test
        @Transactional
        @Order(3)
        void updateReseauSociaux400() {
            ReseauSociauxInDto parkingInDto = new ReseauSociauxInDto("Test Update ReseauSociaux@123");

            ApiError errorResponse = given()
                    .header("apikey", apiKey)
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .put("http://localhost:8081/api/reseausociaux/" + indexCreateMethod)
                    .then()
                    .statusCode(400)
                    .extract()
                    .body()
                    .as(ApiError.class);

            assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
            assertEquals("champ : libelleReseau - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
        }
        @Test
        @Transactional
        void updateReseauSociaux404() {
            ReseauSociauxInDto parkingInDto = new ReseauSociauxInDto("Test Update ReseauSociaux");

            String errorMessage = given()
                    .header("apikey", apiKey)
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .put("http://localhost:8081/api/reseausociaux/" + 1000000000)
                    .then()
                    .statusCode(404)
                    .extract()
                    .body()
                    .asString();

            assertEquals("ReseauSociaux non trouvé", errorMessage);
        }

        @Test
        @Transactional
        void deleteReseauSociaux404() {
            String errorMessage = given()
                    .header("apikey", apiKey)
                    .when()
                    .delete("http://localhost:8081/api/reseausociaux/" + 1000000000)
                    .then()
                    .statusCode(404)
                    .extract()
                    .body()
                    .asString();

            assertEquals("ReseauSociaux non trouvé", errorMessage);
        }

        @Test
        @Transactional
        @Order(4)
        void deleteReseauSociaux200() {
            String errorMessage = given()
                    .header("apikey", apiKey)
                    .when()
                    .delete("http://localhost:8081/api/reseausociaux/" + indexCreateMethod)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

            assertEquals("ReseauSociaux Supprimé", errorMessage);
        }




    }