package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.controller.exception.ApiError;
import fr.rp.springapiparc.dto.in.ParkingInDto;
import fr.rp.springapiparc.dto.out.ParkingOutDto;
import fr.rp.springapiparc.entity.ParkingEntity;
import fr.rp.springapiparc.repository.ParkingRepository;
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
class ParkingControllerTest {

        @Autowired
        ParkingRepository parkingRepository;
        List<ParkingEntity> listParkingEntity = new ArrayList<>();
        List<ParkingOutDto> listParkingOutDto = new ArrayList<>();
        Random rand = new Random();
        ParkingOutDto testControleParkingOutDto;
        static int indexCreateMethod;

        @BeforeEach
        void setUp() {
            listParkingEntity = parkingRepository.findAll();
            for (ParkingEntity parkingEntity : listParkingEntity) {
                ParkingOutDto parkingOutDto = new ParkingOutDto(parkingEntity);
                listParkingOutDto.add(parkingOutDto);
            }
            Optional<ParkingEntity> optionalParkingEntity = parkingRepository.findById(1);
            if (optionalParkingEntity.isPresent()) {
                ParkingEntity testControleparkingEntity = optionalParkingEntity.get();
                testControleParkingOutDto = new ParkingOutDto(testControleparkingEntity);
            }
        }
        @Test
        void getListParking() {
            List<ParkingOutDto> apiResponse = given()
                    .when()
                    .get("http://localhost:8084/api/parkings")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", ParkingOutDto.class);

            assertEquals(listParkingOutDto.size(), apiResponse.size());

            int indexControl = rand.nextInt(listParkingOutDto.size());

            assertEquals(listParkingOutDto.get(indexControl).getId(), apiResponse.get(indexControl).getId());
            assertEquals(listParkingOutDto.get(indexControl).getParking(), apiResponse.get(indexControl).getParking());

        }


        @Test
        void getParkingById200() {
            ParkingOutDto apiResponse = given()
                    .when()
                    .get("http://localhost:8084/api/parkings/" + 1)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ParkingOutDto.class);

            assertEquals(testControleParkingOutDto.getId(), apiResponse.getId());
            assertEquals(testControleParkingOutDto.getParking(), apiResponse.getParking());

        }
        @Test
        void getParkingById404() {
            String errorMessage = given()
                    .when()
                    .get("http://localhost:8084/api/parkings/" + 1000000000)
                    .then()
                    .statusCode(404)
                    .extract()
                    .body()
                    .asString();

            assertEquals("Parking non trouvé", errorMessage);
        }
        @Test
        @Transactional
        @Order(1)

        void createParking201() {
            ParkingInDto parkingInDto = new ParkingInDto("Test Create Parking");

            ParkingOutDto apiResponse = given()
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .post("http://localhost:8084/api/parkings")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ParkingOutDto.class);

            assertEquals(parkingInDto.getParking(), apiResponse.getParking());
            indexCreateMethod = apiResponse.getId();

        }

        @Test
        @Transactional
        void createParking400() {
            ParkingInDto parkingInDto = new ParkingInDto("Test Create Parking1234@");

            ApiError errorResponse = given()
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .post("http://localhost:8084/api/parkings")
                    .then()
                    .statusCode(400)
                    .extract()
                    .body()
                    .as(ApiError.class);

            assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
            assertEquals("champ : parking - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
        }


        @Test
        @Transactional
        @Order(2)
        void updateParking200() {
            ParkingInDto parkingInDto = new ParkingInDto("Test Update Parking");

            ParkingOutDto apiResponse = given()
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .put("http://localhost:8084/api/parkings/"+ indexCreateMethod)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ParkingOutDto.class);

            assertEquals(parkingInDto.getParking(), apiResponse.getParking());
        }

        @Test
        @Transactional
        @Order(3)
        void updateParking400() {
            ParkingInDto parkingInDto = new ParkingInDto("Test Update Parking@123");

            ApiError errorResponse = given()
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .put("http://localhost:8084/api/parkings/" + indexCreateMethod)
                    .then()
                    .statusCode(400)
                    .extract()
                    .body()
                    .as(ApiError.class);

            assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
            assertEquals("champ : parking - erreur : Seules les lettres, les espaces et les tirets sont autorisés \n ", errorResponse.getMessage());
        }
        @Test
        @Transactional
        void updateParking404() {
            ParkingInDto parkingInDto = new ParkingInDto("Test Update Parking");

            String errorMessage = given()
                    .contentType(ContentType.JSON)
                    .body(parkingInDto)
                    .when()
                    .put("http://localhost:8084/api/parkings/" + 1000000000)
                    .then()
                    .statusCode(404)
                    .extract()
                    .body()
                    .asString();

            assertEquals("Parking non trouvé", errorMessage);
        }

        @Test
        @Transactional
        void deleteParking404() {
            String errorMessage = given()
                    .when()
                    .delete("http://localhost:8084/api/parkings/" + 1000000000)
                    .then()
                    .statusCode(404)
                    .extract()
                    .body()
                    .asString();

            assertEquals("Parking non trouvé", errorMessage);
        }

        @Test
        @Transactional
        @Order(4)
        void deleteParking200() {
            String errorMessage = given()
                    .when()
                    .delete("http://localhost:8084/api/parkings/" + indexCreateMethod)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

            assertEquals("Parking Supprimé", errorMessage);
        }

    }