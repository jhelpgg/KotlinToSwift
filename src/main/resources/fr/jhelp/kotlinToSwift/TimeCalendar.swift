import Foundation

class TimeCalendar
{
    private let dateComponents : DateComponents

    init()
    {
        let calendar = Calendar(identifier: .gregorian)
        self.dateComponents = calendar.dateComponents([.year, .month, .day, .weekday, .hour, .minute, .second], from: Date())
    }

    func year() -> Int
    {
        return self.dateComponents.year!
    }

    func month() -> Int
    {
        return self.dateComponents.month!
    }

    func dayOfMonth() -> Int
    {
        return self.dateComponents.day!
    }

    func dayOfWeek() -> Int
    {
        return self.dateComponents.weekday!
    }

    func hour() -> Int
    {
        return self.dateComponents.hour!
    }

    func minute() -> Int
    {
        return self.dateComponents.minute!
    }

    func second() -> Int
    {
        return self.dateComponents.second!
    }
}