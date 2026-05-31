"use client";

import { ChangeEvent, useMemo, useState } from "react";

const ANALYZE_ENDPOINT = "http://localhost:8080/api/resume/analyze";

type ResumeAnalysisResult = {
  score: number;
  summary: string;
  problems: string[];
  suggestions: string[];
  jobFit: {
    backendDevelopment: string;
    testDevelopment: string;
    devOpsSre: string;
  };
};

type ApiErrorResponse = {
  code?: string;
  message?: string;
};

type MessageType = "info" | "success" | "error";

export default function Home() {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [result, setResult] = useState<ResumeAnalysisResult | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState("请选择一份 PDF 简历开始诊断。");
  const [messageType, setMessageType] = useState<MessageType>("info");

  const selectedFileLabel = useMemo(() => {
    if (!selectedFile) {
      return "仅支持 PDF 文件";
    }

    return `${selectedFile.name} · ${(selectedFile.size / 1024).toFixed(1)} KB`;
  }, [selectedFile]);

  function handleFileChange(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0] ?? null;
    setResult(null);

    if (!file) {
      setSelectedFile(null);
      setMessage("请选择一份 PDF 简历开始诊断。");
      setMessageType("info");
      return;
    }

    if (file.type !== "application/pdf" && !file.name.toLowerCase().endsWith(".pdf")) {
      setSelectedFile(null);
      setMessage("当前只支持 PDF 文件。");
      setMessageType("error");
      return;
    }

    setSelectedFile(file);
    setMessage("文件已选择，可以开始诊断。");
    setMessageType("success");
  }

  async function handleAnalyze() {
    if (!selectedFile) {
      setMessage("请先选择 PDF 文件。");
      setMessageType("error");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedFile);

    setIsLoading(true);
    setResult(null);
    setMessage("正在上传简历并请求后端诊断...");
    setMessageType("info");

    try {
      const response = await fetch(ANALYZE_ENDPOINT, {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error(await getErrorMessage(response));
      }

      const data = (await response.json()) as ResumeAnalysisResult;
      setResult(data);
      setMessage("诊断完成，结果已更新。");
      setMessageType("success");
    } catch (error) {
      setMessage(getFriendlyErrorMessage(error));
      setMessageType("error");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <main className="page">
      <section className="intro" aria-labelledby="page-title">
        <p className="eyebrow">Computer Science Resume Diagnosis</p>
        <h1 id="page-title">AI Resume Review</h1>
        <p className="description">
          面向计算机应届生的 AI 简历诊断工具。选择 PDF 简历后，前端会上传文件到 Spring Boot 后端并展示诊断结果。
        </p>
      </section>

      <section className="workspace" aria-label="简历诊断工作区">
        <div className="panel upload-panel">
          <div className="section-heading">
            <h2>上传简历</h2>
            <span>PDF</span>
          </div>

          <label className="upload-zone">
            <input
              type="file"
              accept="application/pdf,.pdf"
              onChange={handleFileChange}
              disabled={isLoading}
            />
            <strong>{selectedFile ? "已选择文件" : "选择 PDF 简历"}</strong>
            <span>{selectedFileLabel}</span>
          </label>

          <div className="actions">
            <button type="button" onClick={handleAnalyze} disabled={!selectedFile || isLoading}>
              {isLoading ? "诊断中..." : "开始诊断"}
            </button>
            <p className={`status-message ${messageType}`} role="status">
              {message}
            </p>
          </div>
        </div>

        <div className="panel result-panel">
          <div className="section-heading">
            <h2>诊断结果</h2>
            <span>{result ? `${result.score}/100` : isLoading ? "处理中" : "等待上传"}</span>
          </div>

          {result ? (
            <div className="result-content">
              <p className="summary">{result.summary}</p>

              <ResultList title="主要问题" items={result.problems} />
              <ResultList title="修改建议" items={result.suggestions} />

              <div className="job-fit">
                <h3>岗位匹配度</h3>
                <dl>
                  <div>
                    <dt>后端开发</dt>
                    <dd>{result.jobFit.backendDevelopment}</dd>
                  </div>
                  <div>
                    <dt>测试开发</dt>
                    <dd>{result.jobFit.testDevelopment}</dd>
                  </div>
                  <div>
                    <dt>DevOps/SRE</dt>
                    <dd>{result.jobFit.devOpsSre}</dd>
                  </div>
                </dl>
              </div>
            </div>
          ) : (
            <div className="empty-state">
              <strong>{isLoading ? "正在生成诊断结果" : "暂无诊断结果"}</strong>
              <p>
                {isLoading
                  ? "后端正在处理上传的 PDF 文件，请稍候。"
                  : "上传 PDF 简历并点击“开始诊断”后，这里会展示评分、问题、建议和岗位匹配度。"}
              </p>
            </div>
          )}
        </div>
      </section>
    </main>
  );
}

async function getErrorMessage(response: Response) {
  const fallback = `诊断请求失败，HTTP 状态码：${response.status}`;
  const contentType = response.headers.get("content-type") ?? "";

  if (!contentType.includes("application/json")) {
    return fallback;
  }

  try {
    const data = (await response.json()) as ApiErrorResponse;
    return data.message || data.code || fallback;
  } catch {
    return fallback;
  }
}

function getFriendlyErrorMessage(error: unknown) {
  if (error instanceof TypeError) {
    return "无法连接后端接口，请确认 Spring Boot 服务已在 http://localhost:8080 启动，并允许前端访问。";
  }

  if (error instanceof Error) {
    return error.message;
  }

  return "诊断失败，请稍后重试。";
}

function ResultList({ title, items }: { title: string; items: string[] }) {
  return (
    <div className="result-list">
      <h3>{title}</h3>
      <ul>
        {items.map((item) => (
          <li key={item}>{item}</li>
        ))}
      </ul>
    </div>
  );
}
