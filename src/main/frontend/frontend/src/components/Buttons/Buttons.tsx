import axios from "axios";

export interface ButtonsProps {
  setLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

const Buttons: React.FC<ButtonsProps> = ({ setLoading }) => {
  const deleteAll = () => {
    const api = process.env.REACT_APP_UPLOAD_API!;
    setLoading(true);
    axios
      .post(api + "/deleteFiles")
      .then((response) => {
        setLoading(false);
        console.log(response);
      })
      .catch((err) => console.log(err));
  };
  return (
    <>
      <button onClick={deleteAll}>Delete all pictures</button>
    </>
  );
};

export default Buttons;
