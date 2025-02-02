package ca.bc.gov.educ.api.penRequest.service;

import ca.bc.gov.educ.api.penRequest.model.PenRequestEntity;
import ca.bc.gov.educ.api.penRequest.repository.PenRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.bc.gov.educ.api.penRequest.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.penRequest.exception.InvalidParameterException;
import ca.bc.gov.educ.api.penRequest.props.ApplicationProperties;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PenRequestService {

    @Autowired
    private PenRequestRepository penRequestRepository;

    public PenRequestEntity retrievePenRequest(UUID id) throws EntityNotFoundException{
        Optional<PenRequestEntity> res = penRequestRepository.findById(id);
        if(res.isPresent()){
            return res.get();
        } else {
            throw new EntityNotFoundException(PenRequestEntity.class, "penRequestId", id.toString());
        }
    }

    public PenRequestEntity createPenRequest(PenRequestEntity penRequest) throws EntityNotFoundException, InvalidParameterException {
        validateParameters(penRequest);
        
        if(penRequest.getPenRequestID()!=null){
            throw new InvalidParameterException("penRequest");
        }
        penRequest.setPenRequestStatusCode("INITREV");
        penRequest.setUpdateUser(ApplicationProperties.CLIENT_ID);
        penRequest.setUpdateDate(new Date());
        penRequest.setCreateUser(ApplicationProperties.CLIENT_ID);
        penRequest.setCreateDate(new Date());

        return penRequestRepository.save(penRequest);
    }

    public Iterable<PenRequestEntity> retrieveAllRequests() throws EntityNotFoundException {
        if(penRequestRepository.findAll() == null) {
            throw new EntityNotFoundException(PenRequestEntity.class, "penRequestId", "any");
        }
        return penRequestRepository.findAll();
    }

    public PenRequestEntity updatePenRequest(PenRequestEntity penRequest) throws EntityNotFoundException, InvalidParameterException {
        
        validateParameters(penRequest);
        
        Optional<PenRequestEntity> curPenRequest = penRequestRepository.findById(penRequest.getPenRequestID());

        if(curPenRequest.isPresent())
        {
            PenRequestEntity newPenRequest = curPenRequest.get();
            newPenRequest.setPenRequestStatusCode(penRequest.getPenRequestStatusCode());
            newPenRequest.setLegalFirstName(penRequest.getLegalFirstName());
            newPenRequest.setLegalLastName(penRequest.getLegalLastName());
            newPenRequest.setDob(penRequest.getDob());
            newPenRequest.setGenderCode(penRequest.getGenderCode());
            newPenRequest.setDataSourceCode(penRequest.getDataSourceCode());
            newPenRequest.setUsualFirstName(penRequest.getUsualFirstName());
            newPenRequest.setUsualMiddleName(penRequest.getUsualMiddleName());
            newPenRequest.setUsualLastName(penRequest.getUsualLastName());
            newPenRequest.setEmail(penRequest.getEmail());
            newPenRequest.setMaidenName(penRequest.getMaidenName());
            newPenRequest.setPastNames(penRequest.getPastNames());
            newPenRequest.setLastBCSchool(penRequest.getLastBCSchool());
            newPenRequest.setLastBCSchoolStudentNumber(penRequest.getLastBCSchoolStudentNumber());
            newPenRequest.setCurrentSchool(penRequest.getCurrentSchool());
            newPenRequest.setReviewer(penRequest.getReviewer());
            newPenRequest.setUpdateUser(ApplicationProperties.CLIENT_ID);
            newPenRequest.setUpdateDate(new Date());
            newPenRequest = penRequestRepository.save(newPenRequest);

            return newPenRequest;
        } else {
            throw new EntityNotFoundException(PenRequestEntity.class, "PenRequest", penRequest.getPenRequestID().toString());
        }
    }

    private void validateParameters(PenRequestEntity penRequestEntity) throws InvalidParameterException {

        if(penRequestEntity.getCreateDate()!=null)
            throw new InvalidParameterException("createDate");
        if(penRequestEntity.getCreateUser()!=null)
            throw new InvalidParameterException("createUser");
        if(penRequestEntity.getUpdateDate()!=null)
            throw new InvalidParameterException("updateDate");
        if(penRequestEntity.getUpdateUser()!=null)
            throw new InvalidParameterException("updateUser");
    }
}
