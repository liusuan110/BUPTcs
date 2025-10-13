from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api import recommendation, chat, analysis
import uvicorn

app = FastAPI(
    title="CS Learning AI Service",
    description="AI服务用于智能推荐、聊天和学习分析",
    version="1.0.0"
)

# CORS中间件
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],  # 前端地址
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 注册路由
app.include_router(recommendation.router, prefix="/api/v1/recommendation", tags=["recommendation"])
app.include_router(chat.router, prefix="/api/v1/chat", tags=["chat"])
app.include_router(analysis.router, prefix="/api/v1/analysis", tags=["analysis"])

@app.get("/")
async def root():
    return {"message": "CS Learning AI Service is running!"}

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )