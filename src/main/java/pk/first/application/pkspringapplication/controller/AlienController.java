package pk.first.application.pkspringapplication.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.first.application.pkspringapplication.dto.SpaceDto;
import pk.first.application.pkspringapplication.model.Space;
import pk.first.application.pkspringapplication.repository.SpaceRepository;
import pk.first.application.pkspringapplication.response.ResponseHandler;
import pk.first.application.pkspringapplication.service.SpaceService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(
    path="/api/alien",
    consumes = {"*/*"},
//    consumes    = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_FORM_URLENCODED_VALUE},
    produces    = MediaType.APPLICATION_JSON_VALUE,
    method      = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE}
)
public class AlienController {

    private final SpaceService spaceService;
    private final SpaceRepository spaceRepository;
    @GetMapping("/")
    public  String home(){
        return "Hi this is alien";
    }
    /* Example  without service layer */
    @RequestMapping(path = "/addSpace", method = RequestMethod.POST)
    public ResponseEntity<Space> addSpace(@RequestBody Space spaceDto){
        try {
            Date date = new Date();
            String strDateFormat = "hh:mm:ss a";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);
            Instant instant = Instant.now();

            Space spaceObj =  new Space();
            spaceObj.setGalaxyName(spaceDto.getGalaxyName());
            spaceObj.setNote(spaceDto.getNote());
            spaceObj.setUniverseName(spaceDto.getUniverseName());


            Space createSpace =  spaceRepository.save(spaceObj);
            return new ResponseEntity<>(createSpace,HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/allSpace/{id}")
    public ResponseEntity<Space> getAllSpace(@PathVariable("id") long id){
        Optional<Space> optionalData = spaceRepository.findById(id);
        if(optionalData.isPresent()){
            return new ResponseEntity<>(optionalData.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/allSpace")
    public ResponseEntity<List<Space>> allSpace(){

        List<Space> allListOfSpace = new ArrayList<Space>();

        spaceRepository.findAll().forEach(allListOfSpace::add);
        return  new ResponseEntity<>(allListOfSpace,HttpStatus.OK);
    }

    @DeleteMapping("deleteSpace/{id}")
    public ResponseEntity<HttpStatus> deleteSpace(@PathVariable("id") long id){
        try{
            spaceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("updateSpace/{id}")
    public ResponseEntity<Space> updateSpace(@PathVariable("id") long id, @RequestBody Space space){
        try {
            Optional<Space> updateData = spaceRepository.findById(id);
            if(updateData.isPresent()){
                Space _space = updateData.get();
                _space.setUniverseName(space.getUniverseName());
                _space.setNote(space.getNote());
                _space.setGalaxyName(space.getGalaxyName());
                Space updateSpace = spaceRepository.save(_space);
                return new ResponseEntity<>(updateSpace, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }catch (Exception  e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /* Example  with service layer */
    @GetMapping("/serviceAllSpace")
    public ResponseEntity<List<Space>> serviceAllSpace(){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Responded", "UserController");
            return  ResponseEntity.status(HttpStatus.OK).headers(headers).body(spaceService.getAllSpace());

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/serviceAllSpace/{id}")
    public ResponseEntity<Space> serviceAllSpace(@PathVariable("id") long id){
        try{
            Optional<Space> isAvailable = spaceService.spaceById(id);
            if(isAvailable.isPresent()){
                return new ResponseEntity<>(isAvailable.get(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /* With query HQL */
    @GetMapping("/space/{universeName}")
    public ResponseEntity<List<Space>> getByUniverseName(@PathVariable("universeName") String universeName){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(spaceService.findByUniverseName(universeName));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*Example  with response handler */
    @GetMapping(value = "/responseAllSpace")
    public ResponseEntity<Object> Get() {
        try {
            List<Space> result = spaceService.getAllSpace();
            return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }
    @PostMapping("universeDetails")
    public ResponseEntity<Object> universeDetails(@RequestParam(name = "name") String name){
        try {
            List<Space> universeDetails = spaceRepository.findByUniverseName(name);
            return ResponseHandler.generateResponse("Find  universe details here",HttpStatus.OK,universeDetails);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("universeDetailsByName")
    public ResponseEntity<Object> universeDetailsByName(@RequestBody ObjectNode objectNode ){
        try {
            // And then you can call parameters from objectNode
            String strOne = objectNode.get("uname").asText();

            List<Space> universeDetails = spaceRepository.findByUniverseName(strOne);
            return ResponseHandler.generateResponse("Find  universe details here",HttpStatus.OK,universeDetails);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
