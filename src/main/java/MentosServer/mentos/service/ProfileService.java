package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.PostProfileReq;
import MentosServer.mentos.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    //프로필 등록
    public void createProfile(PostProfileReq postProfileReq) throws BaseException {
        int result = 0;
        int exitMento, exitMenti;

        try{
            exitMento = profileRepository.checkMentoProfile(postProfileReq.getMemberId()); //멘토 프로필 존재 여부
            exitMenti = profileRepository.checkMentiProfile(postProfileReq.getMemberId()); //멘티 프로필 존재 여부
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

        if (exitMento == 1 && exitMenti == 1) { //멘토, 멘티 프로필이 모두 존재하는 경우
            throw new BaseException(POST_DUPLICATED_PROFILE);
        }

        try{
            if ((postProfileReq.getRole() == 1 && exitMento == 0) || ((exitMento != exitMenti) && exitMento == 0)) {//멘토 프로필 생성
                result = profileRepository.createMentoProfile(postProfileReq);
            }
            else if ((postProfileReq.getRole() == 2 && exitMenti == 0) || ((exitMento != exitMenti) && exitMento == 1)) {//멘티 프로필 생성
                result = profileRepository.createMentiProfile(postProfileReq);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

        if(result == 0){
            throw new BaseException(FAILED_TO_SETPROFILE);
        }
    }
}