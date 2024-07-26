export interface ToolboxItem {
    id: string;
    type: string;
    name: string;
    category: string;
    icon?: string;
}

export interface ToolboxCategory {
    type: string;
    name: string;
    items: ToolboxItem[];
}
