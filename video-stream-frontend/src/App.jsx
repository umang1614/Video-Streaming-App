// import { useState } from 'react'
import './App.css'
import "tailwindcss";
import VideoUpload from './component/VideoUpload';
import { useState } from 'react';

function App() {
  const [videoId, setVideoId] = useState("be79ede3-9ead-4b6b-bb21-dd26f290302a");

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
            }} src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`} controls></video>
            {/* <video
              id="my-video"
              class="video-js"
              controls
              preload="auto"
              width="640"
              height="264"
              data-setup="{}"
            >
              <source src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`} type="video/mp4" />
              <p class="vjs-no-js">
                To view this video please enable JavaScript, and consider upgrading to a
                web browser that
                <a href="https://videojs.com/html5-video-support/" target="_blank"
                >supports HTML5 video</a
                >
              </p>
            </video> */}
          </div>
          <VideoUpload />
        </div>
      </div>
    </>
  )
}

export default App
