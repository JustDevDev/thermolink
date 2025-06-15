export type RequestLoginType = {
  email: string;
  password: string;
};

export type RequestRegisterType = {
  email: string;
  password: string;
};

export type RequestUserUpdateTypes = {
  userAvatar?: string;
  userFirstName?: string;
  userLastName?: string;
  userCurrentPassword?: string;
  userNewPassword?: string;
  hintPassed?: boolean;
};

export type RequestLogoutType = {
  userId: string;
};

export type RequestForgottenPasswordType = {
  email: string;
};

export type RequestForgottenPasswordNewType = {
  password: string;
  resetPasswordToken: string;
};
