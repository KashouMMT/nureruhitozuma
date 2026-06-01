package com.dev.main.serviceImpl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import com.dev.main.dto.AnnouncementDto;
import com.dev.main.model.Announcement;
import com.dev.main.model.User;
import com.dev.main.repository.AnnouncementRepository;
import com.dev.main.service.AnnouncementService;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{

	private final AnnouncementRepository announcementRepo;
	
	public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
		super();
		this.announcementRepo = announcementRepository;
	}
	
	@Override
	public List<Announcement> getAllAnnouncements() {
		return announcementRepo.findAll();
	}

	@Override
	public Announcement getAnnouncementById(Long id) {
		return announcementRepo.findById(id).orElse(null);
	}

	@Override
	public void createAnnouncement(AnnouncementDto announcementDto,User user) {
		Announcement announcement = new Announcement();
		announcement.setTitle(announcementDto.getTitle());
		announcement.setContent(announcementDto.getContent());
		announcement.setUser(user);
		announcementRepo.save(announcement);
	}

	@Override
	public void editAnnouncement(Long id, AnnouncementDto announcementDto,User user) {
		Announcement announcement = getAnnouncementById(id);
		if(Objects.isNull(announcement) || announcement == null) return;
		announcement.setTitle(announcementDto.getTitle());
		announcement.setContent(announcementDto.getContent());
		announcement.setUser(user);
		announcementRepo.save(announcement);
	}

	@Override
	public void deleteAnnouncement(Long id) {
		announcementRepo.deleteById(id);
	}

}
