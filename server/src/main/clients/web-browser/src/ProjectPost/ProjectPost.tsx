import {ReactNode} from 'react';

function ProjectPost({
  title,
  date,
  image,
  content,
}: {
  title: string;
  date: Date;
  image: string;
  content: ReactNode;
}) {
  return (
    <article>
      <div className="post-header">
        <h2>{title}</h2>
        <span>{date.toLocaleDateString()}</span>
      </div>
      <img src={image} alt={title} />
      {content}
    </article>
  );
}

export {ProjectPost};
