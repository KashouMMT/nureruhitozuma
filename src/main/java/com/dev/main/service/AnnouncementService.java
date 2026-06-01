package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.AnnouncementDto;
import com.dev.main.model.Announcement;
import com.dev.main.model.User;

public interface AnnouncementService {
	List<Announcement> getAllAnnouncements();
	
	Announcement getAnnouncementById(Long id);
	
	void createAnnouncement(AnnouncementDto announcementDto,User user);
	void editAnnouncement(Long id, AnnouncementDto announcementDto,User user);
	void deleteAnnouncement(Long id);
	
}
