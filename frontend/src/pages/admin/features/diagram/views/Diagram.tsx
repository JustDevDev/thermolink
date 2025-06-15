import { FC, useEffect } from "react";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { useDiagramStore } from "../hooks/useDiagramStore";
import DiagramCanvas from "../components/DiagramCanvas";

const Diagram: FC = () => {
  const store = useDiagramStore();
  const userStore = useUserStore();

  useEffect(() => {
    if (!store.isConnected) {
      store.onConnect();
    }

    return () => {
      store.onDisconnect();
    };
  }, [store]);

  useEffect(() => {
    const loadDiagramData = async () => {
      await store.getSourceDiagramData();
    };

    loadDiagramData();
  }, [store]);

  useEffect(() => {
    userStore.setBasicViewDefaultMaxWidth();

    return () => {
      userStore.setBasicViewDefaultMaxWidth();
    };
  }, [userStore]);

  return <DiagramCanvas />;
};

export default Diagram;
