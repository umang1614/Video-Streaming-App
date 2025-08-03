// import { useState } from 'react'
import './App.css'
import "tailwindcss";
import VideoUpload from './component/VideoUpload';
import { useState } from 'react';

function App() {
  const [videoId, setVideoId] = useState("04ba5953-60d8-4ea5-9a60-e2c700fc45f8");

  return (
    <>
      <div className='flex flex-col items-center space-y-9 justify-center py-9'>
        <h1 className='text-2xl font-bold text-gray-700 dark:text-gray-100
      '>Video Streaming App</h1>
        <div className='flex w-full justify-around mt-14'>
          <div>
            <span className='text-white'>Video Player</span>
            <video style={{
              width: 500
            }} src={`http://localhost:8080/api/v1/videos/stream/${videoId}`} controls></video>
          </div>
          <VideoUpload />
        </div>
      </div>
    </>
  )
}

export default App
