import { Fragment } from "react";
import { Divider, ListItemIcon, Menu, MenuItem } from "@mui/material";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import NetworkCheckIcon from "@mui/icons-material/NetworkCheck";
import ThermostatIcon from "@mui/icons-material/Thermostat";
import { useDiagramStore } from "../hooks/useDiagramStore";

type DiagramMenuItemsProps = {
  anchorEl: HTMLElement | null;
  open: boolean;
  onClose: () => void;
  getCanvasCenterPosition: () => { x: number; y: number };
};

const DiagramMenuItems = ({
  anchorEl,
  open,
  onClose,
  getCanvasCenterPosition,
}: DiagramMenuItemsProps) => {
  const store = useDiagramStore();

  const menuItems = [
    {
      title: "diagram.temperatureSensor",
      nodeType: "sensorNode",
      onClick: () => {
        const centerPos = getCanvasCenterPosition();
        store.addNode({
          type: "sensorNode",
          position: centerPos,
          data: {
            name: "diagram.temperatureSensor",
            city: {
              id: "",
              city: "",
            },
            temperature: 0,
            averageTemperature: 0,
            condition: "",
            editMode: store.editMode,
          },
        });
        onClose();
      },
      listItemIcon: (
        <ThermostatIcon sx={{ fontSize: "18px" }} color="primary" />
      ),
    },
    {
      title: "diagram.plc",
      nodeType: "plcNode",
      onClick: () => {
        const centerPos = getCanvasCenterPosition();
        store.addNode({
          type: "plcNode",
          position: centerPos,
          data: {
            name: "diagram.plc",
            city: {
              id: "",
              city: "",
            },
            temperature: 0,
            averageTemperature: 0,
            condition: "",
            editMode: store.editMode,
          },
        });
        onClose();
      },
      listItemIcon: (
        <NetworkCheckIcon sx={{ fontSize: "18px" }} color="primary" />
      ),
    },
  ];

  return (
    <Menu
      id="basic-menu"
      anchorEl={anchorEl}
      open={open}
      onClose={onClose}
      MenuListProps={{
        "aria-labelledby": "basic-button",
        autoFocusItem: open,
      }}
      anchorOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      transformOrigin={{
        vertical: "top",
        horizontal: "left",
      }}
      sx={{
        "& .MuiPaper-root": {
          padding: "0 !important",
          marginLeft: "5px !important",
        },
        "& .MuiList-padding": {
          padding: "0 !important",
        },
      }}
    >
      {menuItems.map((item, index) => (
        <Fragment key={index}>
          <MenuItem
            onClick={item.onClick}
            draggable
            onDragStart={(event) => {
              event.dataTransfer.setData(
                "application/reactflow",
                item.nodeType
              );
            }}
            sx={{ padding: "8px 10px", margin: "0", borderRadius: 0 }}
          >
            <ListItemIcon sx={{ minWidth: "0 !important" }}>
              {item.listItemIcon}
            </ListItemIcon>
            <Paragraph
              intld={item.title}
              variant="body2"
              sx={{ marginLeft: "0.5rem " }}
            />
          </MenuItem>
          {index !== menuItems.length - 1 && (
            <Divider sx={{ margin: "0 !important" }} />
          )}
        </Fragment>
      ))}
    </Menu>
  );
};

export default DiagramMenuItems;
