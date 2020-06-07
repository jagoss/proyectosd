package com.sd20.backend.endpoints;

import com.sd20.backend.services.IOTService;
import com.sd20.backend.utils.Gadget;
import com.sd20.backend.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("gadgets")
public class EndpointsController {

    private final IOTService iot;

    @Autowired
    public EndpointsController(IOTService iot){
        this.iot = iot;
    }

    final long MAX_WAIT = 500; //medio segundo

    @PatchMapping("/order")
    public ResponseEntity<List<Gadget>> sendOrder(@RequestBody Request req) {
        iot.sendOrder(req);
        long start = System.nanoTime();
        long time = 0;
        while(iot.getLastRequestOfGadget(req.getDeviceName())!=null && time<MAX_WAIT){
            time = System.nanoTime() - start;
        }
        return ResponseEntity.ok(iot.getCurrentGadgets());
    }

    @GetMapping("/current")
    public ResponseEntity<List<Gadget>> getCurrentGadgets(){
        ResponseEntity<List<Gadget>> re = ResponseEntity.ok(iot.getCurrentGadgets());
        System.out.println(re);
        return re;
    }
}
