package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Post;
import MentosServer.mentos.model.dto.GetMentorSearchReq;
import MentosServer.mentos.model.dto.PostDto;
import MentosServer.mentos.repository.MentorSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MentorSearchService {

	private final MentorSearchRepository mentorSearchRepository;
	
	@Autowired
	public MentorSearchService(MentorSearchRepository mentorSearchRepository) {
		this.mentorSearchRepository = mentorSearchRepository;
	}
	
	// memberId로 선택했던 전공 찾기
	public String getMajorById(String memberId) throws BaseException{
		try{
			return mentorSearchRepository.getMajorById(memberId);
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// memberId로 menti 학교 찾기
	public String getSchoolById(String memberId) throws BaseException{
		try{
			return mentorSearchRepository.getSchoolById(memberId);
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// 검색 결과 반환
	public ArrayList<PostDto> mentorSearch(GetMentorSearchReq req, String schoolId) throws BaseException{
		try{
			List<Post> posts = mentorSearchRepository.getPosts(req, schoolId);
			// UpdateAt으로 정렬 실행
			ArrayList<PostDto> postDtos = sortByUpdateAt(posts);
			return postDtos;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	/**
	 * updateAt 기준으로 정렬 수행
	 * @param arr
	 * @return
	 */
	public ArrayList<PostDto> sortByUpdateAt(List<Post> arr) {
		ArrayList<PostDto> ret = new ArrayList<PostDto>();
		Collections.sort(arr);
		for(Post p : arr){
			ret.add(new PostDto(Integer.toString(p.getPostId()), Integer.toString(p.getMemberId()), p.getPostTitle(), p.getPostContents()));
		}
		return ret;
	}
}
