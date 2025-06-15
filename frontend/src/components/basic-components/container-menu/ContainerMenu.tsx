import MoreVertIcon from "@mui/icons-material/MoreVert";
import ContentCutIcon from "@mui/icons-material/ContentCut";
import { Menu, MenuItem, ListItemIcon, Divider } from "@mui/material";
import { useState, Fragment } from "react";
import Label from "../typography/Label";
import Paragraph from "../typography/Paragraph";
import { observer } from "mobx-react-lite";
import { useSideMenuStore } from "@/components/common-components/menu/hooks/useSideMenuStore";
import IconButton from "../button/IconButton";

export type MenuItemBase = {
  onClick: () => void;
  divider?: boolean;
};

export type TitleMenuItem = MenuItemBase & {
  title: string;
  listItemIcon?: never;
  listItemText?: never;
  listItemSecondaryText?: never;
};

export type CustomMenuItem = MenuItemBase & {
  title?: never;
  listItemIcon?: React.ReactNode;
  listItemText?: string;
  listItemSecondaryText?: string;
  iconPosition?: "start" | "end";
};

export type ContainerMenuProps = {
  menuItems: (TitleMenuItem | CustomMenuItem)[];
};

const ContainerMenu = observer((props: ContainerMenuProps) => {
  const { menuItems } = props;
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const sideMenuStore = useSideMenuStore();

  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
    sideMenuStore.setMenuOptionOpen(true);
  };
  const handleClose = () => {
    setAnchorEl(null);
    sideMenuStore.setMenuOptionOpen(false);
  };

  return (
    <>
      <IconButton onClick={handleClick}>
        <MoreVertIcon sx={{ fontSize: "17px" }} />
      </IconButton>
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={sideMenuStore.isMenuOptionOpen}
        onClose={handleClose}
        MenuListProps={{
          "aria-labelledby": "basic-button",
        }}
      >
        {menuItems.map((item, index) => (
          <Fragment key={index}>
            {item.divider && <Divider />}
            {"title" in item ? (
              <MenuItem onClick={item.onClick}>
                <Paragraph intld={item.title!} variant="body2" />
              </MenuItem>
            ) : (
              <MenuItem
                onClick={item.onClick}
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  gap: 2,
                }}
              >
                {item.iconPosition === "start" && (
                  <ListItemIcon sx={{ minWidth: "0 !important" }}>
                    {item.listItemIcon ? item.listItemIcon : <ContentCutIcon />}
                  </ListItemIcon>
                )}
                <Paragraph intld={item.listItemText!} variant="body2" />
                <Label intld={item.listItemSecondaryText!} />
                {item.iconPosition === "end" && (
                  <ListItemIcon sx={{ minWidth: "0 !important" }}>
                    {item.listItemIcon ? item.listItemIcon : <ContentCutIcon />}
                  </ListItemIcon>
                )}
              </MenuItem>
            )}
          </Fragment>
        ))}
      </Menu>
    </>
  );
});

export default ContainerMenu;
