package com.tcss559.alltollpass.controller;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.model.request.toll.TollRateRequest;
import com.tcss559.alltollpass.model.response.toll.TollRateResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerTransactionResponse;
import com.tcss559.alltollpass.repository.CustomTraceRepository;
import com.tcss559.alltollpass.service.AdminService;
import com.tcss559.alltollpass.service.TollService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "admin-service", description = "AllTollPass Admin Service : A few services for the Administrator at AllTollPass")

public class AdminController {

    @Autowired
    AdminService adminService;

//    // Make a user inactive
//    @DeleteMapping("/user/{username}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteUser(TollRateRequest tollRateRequest){
//        adminService.deleteUser(tollRateRequest);
//    }
//
//    // Get transactions for a given status
//    @GetMapping("/reports/{status}")
//    @ResponseStatus(HttpStatus.OK)
//    public void getReports() throws DatabaseException, RfidNotFoundException {
//       adminService.getAllTranscationsByStatus();
//    }
//
//    // Get user details given username
//    @GetMapping("/user")
//    @ResponseStatus(HttpStatus.OK)
//    public void getReportsByStatus() throws DatabaseException, RfidNotFoundException {
//        adminService.getUserDetails();
//    }

}