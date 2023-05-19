import React, {useRef, useState} from 'react';
import Webcam from 'react-webcam';
import './Upload.scss';

function App() {
  const [image, setImage] = useState<string | null>();
  const videoConstraints = {
    width: window.innerWidth,
    height: window.innerHeight,
  };
  const webcam = useRef<Webcam>(null);

  const takePicture = () => {
    setImage(webcam.current!.getScreenshot());
  };

  return (
    <main>
      <Webcam ref={webcam} videoConstraints={videoConstraints} />
      <button className="camera-button" onClick={takePicture}></button>
      {image !== null && <img src={image} alt="Project" />}
    </main>
  );
}

export default App;
