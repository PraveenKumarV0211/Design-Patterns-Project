type NotificationType = 'info' | 'success' | 'error';
type NotificationHandler = (message: string, type: NotificationType) => void;

// Observer Pattern — Subject that UI components subscribe to
class NotificationService {
    private static instance: NotificationService;
    private observers: NotificationHandler[] = [];

    static getInstance(): NotificationService {
        if (!NotificationService.instance) {
            NotificationService.instance = new NotificationService();
        }
        return NotificationService.instance;
    }

    subscribe(handler: NotificationHandler): () => void {
        this.observers.push(handler);
        return () => {
            this.observers = this.observers.filter(h => h !== handler);
        };
    }

    notify(message: string, type: NotificationType = 'info') {
        this.observers.forEach(h => h(message, type));
    }
}

export default NotificationService.getInstance();
export type { NotificationType };
