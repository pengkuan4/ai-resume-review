package com.airesumereview.backend.resume.diagnosis;

import java.util.List;

import com.airesumereview.backend.resume.JobFitResponse;
import org.springframework.stereotype.Service;

@Service
public class MockResumeDiagnosisService implements ResumeDiagnosisService {

    @Override
    public ResumeDiagnosisResult diagnose(String resumeText) {
        return new ResumeDiagnosisResult(
                72,
                "这是一份 mock 简历诊断结果。当前后端已经完成 PDF 文本提取，但仍未接入真实 AI。",
                List.of(
                        "项目经历描述还不够具体，需要补充个人职责、关键技术实现和项目结果。",
                        "技术栈需要与目标岗位建立更明确的对应关系，避免只堆砌关键词。",
                        "简历中需要突出后端开发、测试开发或 DevOps/SRE 方向的核心能力。"
                ),
                List.of(
                        "每个项目建议按“项目背景、个人职责、技术实现、结果”组织描述。",
                        "补充 Spring Boot、数据库、接口、部署、日志或测试相关的具体实践。",
                        "如果目标是 DevOps/SRE，建议突出 Linux、Docker、Nginx、CI/CD 和故障排查经历。"
                ),
                new JobFitResponse(
                        "中等：需要更多接口开发、数据库设计和后端工程实践细节。",
                        "中等偏低：需要补充测试用例、接口测试、自动化测试或缺陷定位经历。",
                        "中等：如果项目中有 Docker、Nginx、部署和日志排查经历，应在简历中重点展开。"
                )
        );
    }
}
