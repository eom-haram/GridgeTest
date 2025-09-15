package com.tnovel.demo.repository.post;

import com.tnovel.demo.repository.post.entity.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Integer> {
}
