import axios from "axios";

export interface ImageItemProps {
  id: string;
  name: string;
  fullName: string;
  url: string;
  setLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

const ImageItem: React.FC<ImageItemProps> = ({
  name,
  fullName,
  url,
  setLoading,
}) => {
  const api = process.env.REACT_APP_UPLOAD_API!;
  const deleteImage = () => {
    setLoading(true);
    axios
      .post(`${api}/deleteFile/${fullName}`)
      .then((response) => {
        console.log(response);
        setLoading(false);
      })
      .catch((err) => console.log(err));
  };
  console.log(`${api}/image/${fullName}`);

  return (
    <>
      <div>Image name: {name}</div>
      <div>
        <img
          src={url}
          alt={name}
          style={{ maxHeight: "200px", width: "auto" }}
        />
      </div>
      <button onClick={deleteImage}>Delete Image</button>
      <span>
        <a
          href={`${api}/image/${fullName}`}
          download={name}
          target="_blank"
          rel="noreferrer"
        >
          <button>Download: {name}</button>
        </a>
      </span>
    </>
  );
};

export default ImageItem;
