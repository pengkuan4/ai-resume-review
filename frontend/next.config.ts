import type { NextConfig } from "next";

const backendApiUrl = process.env.BACKEND_API_URL ?? "http://backend:8080";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: `${backendApiUrl}/api/:path*`,
      },
    ];
  },
};

export default nextConfig;
