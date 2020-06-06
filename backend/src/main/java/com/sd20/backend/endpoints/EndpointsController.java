package com.sd20.backend.endpoints;

import com.sd20.backend.services.IOTService;
import com.sd20.backend.utils.Gadget;
import com.sd20.backend.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("gadgets")
public class EndpointsController {

    @Autowired
    IOTService iot;

    final long MAX_WAIT = 500; //medio segundo

    @PatchMapping("/order")
    public ResponseEntity<List<Gadget>> sendOrder(@RequestBody Request req) {
        iot.sendOrder(req);
        long start = System.nanoTime();
        long time = 0;
        while(iot.getLastRequestOfGadget(req.getDeviceName())!=null && time<MAX_WAIT){
            time = System.nanoTime() - start;
        }
        return new ResponseEntity<List<Gadget>>(iot.getCurrentGadgets(), HttpStatus.OK);
    }
}
