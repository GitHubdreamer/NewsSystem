package magicgis.newssystem.actions.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javassist.bytecode.stackmap.TypeData.ClassName;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import magicgis.newssystem.actions.BaseAction;
import magicgis.newssystem.constant.WebConstant;
import magicgis.newssystem.models.Comment;
import magicgis.newssystem.models.News;
import magicgis.newssystem.models.NewsType;
import magicgis.newssystem.models.Page;
import magicgis.newssystem.models.State;
import magicgis.newssystem.models.User;
import magicgis.newssystem.models.VisitorCounter;
import magicgis.newssystem.services.CommentService;
import magicgis.newssystem.services.NewsService;
import magicgis.newssystem.services.NewsTypeService;
import magicgis.newssystem.services.StateService;
import magicgis.newssystem.services.VisitorCounterService;
import magicgis.newssystem.utils.CookieUtils;
import magicgis.newssystem.utils.DataUtils;
import magicgis.newssystem.utils.ValidateUtils;

@Controller
@Scope("prototype")
public class NewsAction extends BaseAction<News> {

	private static final long serialVersionUID = 2627607447756718173L;
	@Autowired
	private NewsService newsService;
	@Autowired
	private NewsTypeService newsTypeService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private VisitorCounterService visitorCounterService;
	@Autowired
	private StateService stateService;

	private int maxResult = WebConstant.MAX_RESULT;
	private int result = WebConstant.RESULT;
	private int pageSize = WebConstant.PAGE_SIZE;
	private Integer adminId = null;
	private List<List<News>> allNews = null;
	private List<NewsType> allNewsTypes = null;
	private List<State> allStates = null;
	private List<Comment> allComments = null;
	private Integer newsId = null;;
	private String comment = null;
	private User user = null;
	private VisitorCounter visitorCounter = null;
	private Integer count = null;
	private boolean flag = false;
	private Integer commentId = null;
	private String ipAddress = null;
	private Integer stateId = null;
	private static Logger logger = Logger.getLogger(ClassName.class);
	private String keywords;
	// ����ʱѡ����⻹������
	private int sw;
	private Integer page;
	private Page<News> p;
	// ����ͷ��
	private List<News> headlines = null;
	// �����ʴ�
	private List<News> healthFAQs = null;
	// ҽҩʱѶ
	private List<News> drugslines = null;
	// ������Ѷ
	private List<News> healthlines = null;
	// ����ר��
	private List<News> lifelines = null;
	// �и�ר��
	private List<News> pregnantlines = null;
	// ���в���
	private List<News> infertilitylines = null;
	// ����ר��
	private List<News> genderlines = null;
	// �ȵ��ع�
	private List<News> hotlines = null;

	private File upload;
	private String uploadFileName;
	private String uploadContentType;

	// �û�����ĳ������
	public String publishComment() {
		user = (User) sessionMap.get("user");
		ipAddress = httpRequest.getRemoteAddr();
		commentService.publishComment(comment, ipAddress, newsId, user);
		logger.info(user.getUsername() + "�������ۣ�" + comment + " ,IP��ַ��"
				+ ipAddress + " ,����ID��" + newsId);
		allComments = commentService.getViewNewsAllComment(newsId);
		try {
			writeJSON(allComments);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ����Աɾ��ĳ������
	public String deleteComment() {
		commentService.deleteComment(commentId);
		logger.info("����Աɾ�����ۣ�" + commentId);
		allComments = commentService.getViewNewsAllComment(newsId);
		try {
			writeJSON(allComments);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// �õ�������ͨ��������
	public String getAllPassedNews() {
		headlines = newsService.getRecentNews(1, maxResult);
		healthFAQs = newsService.getRecentNews(3, maxResult);
		drugslines = newsService.getRecentNews(4, result);
		healthlines = newsService.getRecentNews(2, result);
		lifelines = newsService.getRecentNews(5, result);
		pregnantlines = newsService.getRecentNews(6, result);
		infertilitylines = newsService.getRecentNews(7, result);
		genderlines = newsService.getRecentNews(8, result);
		hotlines = newsService.getRecentNews(9, result);

		allNews = new ArrayList<List<News>>();

		allNews.add(headlines);
		allNews.add(healthFAQs);
		allNews.add(drugslines);
		allNews.add(healthlines);
		allNews.add(lifelines);
		allNews.add(pregnantlines);
		allNews.add(infertilitylines);
		allNews.add(genderlines);
		allNews.add(hotlines);

		requestMap.put("allNews", allNews);
		return "to_homePage";
	}

	// �õ�����δͨ��������
	public String getAllNotPassedNews() {
		p = new Page<News>();
		if (page == null) {
			p = newsService.listAllNotPassedNewsPage(1, pageSize);
		} else {
			p = newsService.listAllNotPassedNewsPage(page, pageSize);
		}
		requestMap.put("page", p);
		return "to_examineNewsListPage";
	}

	// �õ����е�����
	public String getAllNews() {
		p = new Page<News>();
		if (page == null) {
			p = newsService.listAllNewsPage(1, pageSize);
		} else {
			p = newsService.listAllNewsPage(page, pageSize);
		}
		requestMap.put("page", p);
		if (sessionMap.get("allNewsTypes") == null) {
			allNewsTypes = newsTypeService.findAllEntities();
			sessionMap.put("allNewsTypes", allNewsTypes);
		}
		if (sessionMap.get("allStates") == null) {
			allStates = stateService.findAllEntities();
			sessionMap.put("allStates", allStates);
		}
		return "to_newsListPage";
	}

	// �ж��û��Ƿ����ָ����cookie,�Դ����������ŵ����
	public void countVisitorNum(Integer newsId) {
		String key = DataUtils.md5(newsId.toString());
		String value = DataUtils.md5("true" + newsId);
		Cookie c = null;
		try {
			c = CookieUtils.getCookie(httpRequest, "COUNT_" + key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (c != null) {
			if (value.equals(c.getValue())) {
				flag = false;
			} else {
				flag = true;
				count++;
			}
		} else {
			flag = true;
			count++;
			CookieUtils.addCookie(httpResponse, "COUNT_" + key, value,
					60 * 60 * 2);
		}
	}

	// ǰ̨�õ�ĳ�����ŵ�ҳ��
	public String getViewNews() {
		model = newsService.getViewNews(model.getId());
		allComments = commentService.getViewNewsAllComment(model.getId());
		requestMap.put("allComments", allComments);
		visitorCounter = new VisitorCounter(httpRequest.getRemoteAddr(),
				new Date(), (User) sessionMap.get("user"), model);
		visitorCounterService.saveEntity(visitorCounter);
		count = model.getCount();
		countVisitorNum(model.getId());
		application.setAttribute("count", count);
		if (flag) {
			model.setCount(count);
			newsService.updateEntity(model);
		}
		return "to_viewNewsPage";
	}

	// ��̨�õ�ĳ�����ŵ�ҳ��
	public String getNews() {
		model = newsService.getViewNews(model.getId());
		allComments = commentService.getViewNewsAllComment(model.getId());
		requestMap.put("allComments", allComments);
		return "to_newsPage";
	}

	// ��̨�ı����ŵ�״̬
	@SuppressWarnings("unchecked")
	public String changeNewsState() {
		model = newsService.getEntity(model.getId());
		allStates = (List<State>) sessionMap.get("allStates");
		State s = allStates.get(sw - 1);
		model.setState(s);
		newsService.updateEntity(model);
		return "to_NewsAction_getAllNotPassedNews";
	}

	// ��̨�õ�ĳ��δͨ�����ŵ�ҳ��
	public String getExamineNews() {
		model = newsService.getViewNews(model.getId());
		if (sessionMap.get("allNewsTypes") == null) {
			allNewsTypes = newsTypeService.findAllEntities();
			sessionMap.put("allNewsTypes", allNewsTypes);
		}
		if (sessionMap.get("allStates") == null) {
			allStates = stateService.findAllEntities();
			sessionMap.put("allStates", allStates);
		}
		return "to_examineNewsPage";
	}

	// ��̨����ĳ������
	public String updateNews() {
		if (model.getId() != null && model.getId() > 0) {
			model = newsService.getEntity(model.getId());
		}
		if (sessionMap.get("allNewsTypes") == null) {
			allNewsTypes = newsTypeService.findAllEntities();
			sessionMap.put("allNewsTypes", allNewsTypes);
		}
		return "to_updateNewsPage";
	}

	// �ϴ�����ͼ
	private void uploadThumbnail(String uploadFileName) throws IOException {
		String ext = uploadFileName.substring(uploadFileName.lastIndexOf("."));
		long filename = System.nanoTime();
		String dir = application.getRealPath("/files/" + filename + ext);
		System.out.println(dir);
		FileOutputStream out = new FileOutputStream(dir);
		FileInputStream in = new FileInputStream(upload);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();
		model.setThumbnail("/files/" + filename + ext);
	}

	// ��̨����ĳ������
	public String saveNews() throws IOException {
		System.out.println("uploadFileName" + uploadFileName);
		if (ValidateUtils.isValid(uploadFileName)) {
			try {
				uploadThumbnail(uploadFileName);
			} catch (Exception e) {
				addFieldError("logoPhoto", "ͼƬ�ϴ�ʧ��");
				System.out.println("ͼƬ�ϴ�ʧ��");
				e.printStackTrace();
				return "upload_error";
			}
		}
		if (stateId != null && model.getId() != null) {
			State s = stateService.getEntity(stateId);
			model.setState(s);//���ø��º��model
			System.out.println(model.toString());
		}
		newsService.saveNews(model, adminId);
		logger.info("����Ա�������ţ�" + model.getTitle());
		return "to_NewsAction_getAllNews";
	}

	// ��̨ɾ��ĳ������
	public String deleteNews() {
		try {
			if (model.getId() != null && model.getId() > 0) {
				newsService.deleteNews(model.getId());
				inputStream = new ByteArrayInputStream("1".getBytes("UTF-8"));
				logger.info("����Աɾ�����ţ�" + model.getTitle());
			}
		} catch (Exception e) {
			try {
				inputStream = new ByteArrayInputStream("0".getBytes("UTF-8"));
				e.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		return "ajax_success";
	}

	// ��̨����ĳ������
	public String addNews() {
		if (sessionMap.get("allNewsTypes") == null) {
			allNewsTypes = newsTypeService.findAllEntities();
			sessionMap.put("allNewsTypes", allNewsTypes);
		}
		return "to_addNewsPage";
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public int getSw() {
		return sw;
	}

	public void setSw(int sw) {
		this.sw = sw;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

}
