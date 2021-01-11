import { useState } from "react";
import ImageList from "./components/ImageList/ImageList";
import MultiFileUpload from "./components/MultiFileUpload/MultiFileUpload";
import MyDropzone from "./components/MyDropzone/MyDropzone";
import Buttons from "./components/Buttons/Buttons";

export interface AppProps {}

const App: React.FC<AppProps> = () => {
  const [loading, setLoading] = useState<boolean>(false);

  return (
    <div className="App">
      <MyDropzone setLoading={setLoading} />
      <hr />
      <MultiFileUpload setLoading={setLoading} />
      <hr />
      <Buttons setLoading={setLoading} />
      <hr />
      <ImageList loading={loading} setLoading={setLoading} />
      <hr />
      {loading}
    </div>
  );
};

export default App;
