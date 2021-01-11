import axios from "axios";

export interface MultiFileUploadProps {
  setLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

const MultiFileUpload: React.FC<MultiFileUploadProps> = ({ setLoading }) => {
  const api = process.env.REACT_APP_UPLOAD_API!;
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const formData = new FormData();
    const inputElement = e.target as HTMLInputElement;
    const files = inputElement.files;
    if (files) {
      Array.from(files).forEach((file) => {
        formData.append(`files`, file);
      });
      setLoading(true);
      axios
        .post(`${api}/multiple`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((respose) => {
          console.log(respose);
          setLoading(false);
        });
    }
  };

  return (
    <div className="App">
      <form>
        <input onChange={handleChange} multiple type="file" name="files" />
      </form>
    </div>
  );
};

export default MultiFileUpload;
