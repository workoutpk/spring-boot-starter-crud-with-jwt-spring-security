package pk.first.application.pkspringapplication.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pk.first.application.pkspringapplication.model.Space;
import pk.first.application.pkspringapplication.repository.SpaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SpaceService {
    private final SpaceRepository  spaceRepository;

    public List<Space> getAllSpace(){
        List<Space> allListOfSpace = new ArrayList<Space>();
        spaceRepository.findAll().forEach(allListOfSpace::add);
        return allListOfSpace;
    }

    public Optional<Space> spaceById(Long id){
        return spaceRepository.findById(id);
    }

    public List<Space> findByUniverseName(String string){
        List<Space> listOfUniverse = new ArrayList<>();
        spaceRepository.findByUniverseName(string).forEach(listOfUniverse::add);
        return listOfUniverse;
    }
}
