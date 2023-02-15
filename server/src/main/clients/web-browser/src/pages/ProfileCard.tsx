import './ProfileCard.scss';

function ProfileCard({ title, imageSrc, description }: {
    title: string,
    imageSrc: string,
    description: string | string[]
}) {
    return (<div className="profile-card">
        <img src={imageSrc} alt={title} />
        <h3>{title}</h3>
        {(typeof description == 'string' ? [description] : description).map(e => <p>{e}</p>)}
    </div>);
}

export default ProfileCard;
