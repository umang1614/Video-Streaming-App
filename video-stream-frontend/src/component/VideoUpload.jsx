import React, { useState } from 'react'
import video from '../assets/video.png';
import { Alert, Button, Card, Label, Progress, Textarea, TextInput } from 'flowbite-react';
import axios from "axios";

function VideoUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [meta, setMeta] = useState({
    title: '',
    description: '',
  })
  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");

  function handleFileChange(event) {
    setSelectedFile(event.target.files[0]);
    console.log("File Selected");

  }

  function formFieldChange(formEvent) {
    setMeta({
      ...meta,
      [formEvent.target.name]: formEvent.target.value,
    })
    setMessage("")
  }
  function handleForm(formEvent) {
    if (!selectedFile) {
      alert("Please select the file!!!")
    }
    formEvent.preventDefault();
    saveVideoToServer(selectedFile, meta);
  }

  function resetForm() {
    setMeta({
      title: '',
      description: ''
    })
    setProgress(0)
    setUploading(false)
    setSelectedFile(null)
  }
  async function saveVideoToServer(video, videoMeta) {
    setUploading(true);
    try {
      let formData = new FormData();
      formData.append("title", videoMeta.title);
      formData.append("description", videoMeta.description);
      formData.append("file", video);
      let response = await axios.post("http://localhost:8080/api/v1/videos", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        onUploadProgress: (progressEvent) => {
          const percentCompleted = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total
          );
          setProgress(percentCompleted);
        }
      })
      setMessage("File uploaded successfully")
      resetForm();
      console.log(response);
    }
    catch (error) {
      setUploading(false);
      setMessage("Error in uploading file")
      console.log(error);

    }
  }
  return (
    <div className='text-white'>
      <Card className='flex flex-col'>
        <h1>Upload Video</h1>
        <div>
          <form noValidate className='flex flex-col space-y-5' onSubmit={handleForm}>
            <div>
              <div className='mb-2 block'>
                <Label htmlFor="comment">Video Title</Label>
              </div>
              <TextInput name='title' placeholder='Enter Title' onChange={formFieldChange} value={meta.title} />
            </div>
            <div className="max-w-md">
              <div className="mb-2 block">
                <Label htmlFor="comment">Video Description</Label>
              </div>
              <Textarea name="description" id="comment" placeholder="Write video description" required rows={4} onChange={formFieldChange} value={meta.description} />
            </div>
            <div className='flex items-center space-x-5'>
              <div className="shrink-0">
                <img className="h-16 w-16 object-cover rounded-full" src={video} alt="Current profile photo" />
              </div>
              <label className="block">
                <span className="sr-only">Choose profile photo</span>
                <input name='file' type="file" className="block w-full text-sm text-slate-500
      file:mr-4 file:py-2 file:px-6
      file:rounded-full file:border-0
      file:text-sm file:font-semibold
      file:bg-amber-500 file:text-white
      hover:file:bg-amber-600
    "
                  onChange={handleFileChange} />
              </label>
            </div>
            <div>
              {uploading && (<Progress
                progress={progress}
                textLabel='Uploading..'
                size={"lg"}
                labelProgress
                labelText
                color={'yellow'}
                label={`${progress}%`}
              />)}
            </div>
            <div>
              {message && (<Alert color={'success'} onDismiss={() => setMessage("")}>
                <span className='font-medium'>Success Alert! </span>{message}
              </Alert>)}
            </div>
            <div className='flex justify-center'>
              <Button disabled={uploading} type='submit' className="bg-amber-500 text-white px-4 py-2 rounded-lg hover:bg-amber-600 cursor-pointer">Submit</Button>
            </div>
          </form>
        </div>
      </Card>
    </div>
  )
}

export default VideoUpload
