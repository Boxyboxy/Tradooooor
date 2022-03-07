export interface IEXquote {
  companyName: string;
  symbol: string;
  price: number;
}

export interface Order {
  orderId?: number;
  email: string;
  symbol: string;
  shares: number;
  price: number;
  timeStamp?: string;
}

export interface User {
  email: string;
  cash?: number;
}

export interface Message {
  message?: string;
  error?: string;
}

export interface Shares {
  symbol: string;
  quantity: number;
  totalCost: number;
  currentPrice?: number;
  quotes?: IEXquote;
}
