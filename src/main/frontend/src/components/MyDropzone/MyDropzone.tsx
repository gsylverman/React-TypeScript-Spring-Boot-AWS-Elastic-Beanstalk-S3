import axios from "axios";
import { useCallback } from "react";
import { useDropzone } from "react-dropzone";

export interface MyDropzoneProps {
  setLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

const MyDropzone: React.FC<MyDropzoneProps> = ({ setLoading }) => {
  const api = process.env.REACT_APP_UPLOAD_API!;
  const onDrop = useCallback(
    (files) => {
      console.log(files);
      const formData = new FormData();
      formData.append("file", files[0]);
      setLoading(true);
      axios
        .post(api, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(() => {
          console.log("uploaded succesfully");
          setLoading(false);
        })
        .catch((err) => console.log(err));
    },
    [api, setLoading]
  );
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()} style={{ textAlign: "center" }}>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the files here ...</p>
      ) : (
        <p>Drag 'n' drop some files here, or click to select files</p>
      )}
    </div>
  );
};

export default MyDropzone;
