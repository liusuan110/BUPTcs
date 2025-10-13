import React from 'react'
import { Routes, Route } from 'react-router-dom'
import { Layout } from 'antd'
import Navbar from './components/Navbar'
import Home from './pages/Home'
import Dashboard from './pages/Dashboard'
import Courses from './pages/Courses'
import Practice from './pages/Practice'
import Profile from './pages/Profile'
import Login from './pages/Login'

const { Content } = Layout

function App() {
  return (
    <Layout className="min-h-screen">
      <Navbar />
      <Content className="p-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/courses" element={<Courses />} />
          <Route path="/practice" element={<Practice />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </Content>
    </Layout>
  )
}

export default App